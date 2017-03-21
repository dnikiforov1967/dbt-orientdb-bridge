/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adition.middleware.dbt.orientdb.bridge.core;

import com.adition.middleware.dbt.orientdb.bridge.annotation.Vertex;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.orientechnologies.orient.core.metadata.schema.OType;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Transient;

/**
 * The class is intended to provide the mapping between Entity and Vertex
 *
 * @author dnikiforov
 */
public final class EntityAnalyzer {
	
	private static ImmutableMap<Class<?>, OType> typesMap;
	
	static {
		Collection<Entry<Class<?>, OType>> listOfEntries = new LinkedList<>();
		listOfEntries.add(Maps.immutableEntry(String.class, OType.STRING));
		listOfEntries.add(Maps.immutableEntry(Integer.class, OType.INTEGER));
	}
	

	public static class GFieldDescriptor {
		private Field field;
		private Class<?> typeOfField;
		
		private void identifyGType() {
			 field.getType();
		}
	}
	
	
	public static class GraphSchema {
		private Class<?> entityClass;
		private ImmutableSet<Field> setOfFields;
		private int totalFieldCount;

		public Class<?> getEntityClass() {
			return entityClass;
		}

		public ImmutableSet<Field> getSetOfFields() {
			return setOfFields;
		}

		public int getTotalFieldCount() {
			return totalFieldCount;
		}

		@Override
		public int hashCode() {
			int hash = 3;
			hash = 97 * hash + Objects.hashCode(this.entityClass);
			return hash;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			final GraphSchema other = (GraphSchema) obj;
			if (!Objects.equals(this.entityClass, other.entityClass)) {
				return false;
			}
			return true;
		}
		
	}

	private EntityAnalyzer() {

	}

	public static EntityAnalyzer newInstance() {
		return new EntityAnalyzer();
	}

	public GraphSchema createSchema(Class<?> clazz) {
		if (!isEntity(clazz)) {
			throw new IllegalArgumentException("Class " + clazz.getName() + " is not Entity");
		}
		
		GraphSchema graphSchema = new GraphSchema();
		graphSchema.entityClass=clazz;
		
		//Default analysis is based on the fields
		final Collection<Field> allFields = getAllFields(clazz);
		graphSchema.totalFieldCount=allFields.size();
		
		final HashSet<Field> fieldsSet = Sets.newHashSet(allFields);
		final Set<Field> filtered = filterNegative(fieldsSet, Transient.class);
		graphSchema.setOfFields=ImmutableSet.copyOf(filtered);
		
		return graphSchema;
	}

	/**
	 * Method checks if the class with @Link Entity annotated
	 *
	 * @param clazz
	 * @return
	 */
	private boolean isEntity(Class<?> clazz) {
		return clazz.isAnnotationPresent(Vertex.class);
	}
	
	/**
	 * Method allows to get all fields of class including fields what have been inherited
	 * 
	 * @param clazz 
	 */
	private Collection<Field> getAllFields(Class<?> clazz) {
		Collection<Field> listOfFields = new LinkedList<>();
		while(clazz!=null) {
			final Field[] declaredFields = clazz.getDeclaredFields();
			listOfFields.addAll(Arrays.asList(declaredFields));
			//When we reach Object, the method returns null
			clazz=clazz.getSuperclass();
		}
		return listOfFields;
	}
	
	/**
	 * Method filters only those fields what are not annotated with certain annotation
	 * @param initialSet
	 * @param annotation
	 * @return 
	 */
	private Set<Field> filterNegative(Set<Field> initialSet, Class<? extends Annotation> annotation) {
		final Set<Field> filtered = Sets.filter(initialSet, new Predicate<Field>() {
			@Override
			public boolean apply(Field field) {
				return !field.isAnnotationPresent(annotation);
			}
		});
		return filtered;
	}
	
}
