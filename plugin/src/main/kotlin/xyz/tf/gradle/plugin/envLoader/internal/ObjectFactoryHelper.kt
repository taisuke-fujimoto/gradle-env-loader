package xyz.tf.gradle.plugin.envLoader.internal

import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty

internal inline fun <reified T> ObjectFactory.property(defaultValue: () -> T): Property<T> =
    property(T::class.java).value(defaultValue())

internal inline fun <reified T> ObjectFactory.listProperty(defaultValue: () -> List<T>): ListProperty<T> =
    listProperty(T::class.java).value(defaultValue())

internal inline fun <reified T> ObjectFactory.setProperty(defaultValue: () -> Set<T>): SetProperty<T> =
    setProperty(T::class.java).value(defaultValue())
