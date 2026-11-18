# V Scanner ProGuard Rules

# Keep Hilt classes
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }
-keep class * extends dagger.hilt.android.internal.managers.ComponentSupplier { *; }
-keep class * extends dagger.hilt.android.internal.managers.ViewComponentManager$FragmentContextWrapper { *; }

# Keep Room entities
-keep class com.vsecurity.scanner.data.model.** { *; }
-keep class * extends androidx.room.RoomDatabase { *; }

# Keep Kotlin serialization
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt

# Keep data classes for reflection
-keepclassmembers class com.vsecurity.scanner.data.model.* {
    <fields>;
    <init>(...);
}

# Keep ViewModels
-keep class com.vsecurity.scanner.ui.viewmodel.** { *; }

# Keep Compose
-keep class androidx.compose.** { *; }

# Keep Material Design
-keep class com.google.android.material.** { *; }

# Standard Android optimizations
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose

# Remove logging in release
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
}

# --- GSON SPECIFIC RULES ---
# Keep the generic signatures so TypeToken can work at runtime
-keepattributes Signature
-keepattributes *Annotation*
-keepattributes EnclosingMethod
-keepattributes InnerClasses

# Prevent Gson from obfuscating the TypeConverter logic
-keep class com.vsecurity.scanner.data.local.Converters { *; }

# Prevent Gson from breaking the TypeToken class itself
-keep class com.google.gson.reflect.TypeToken { *; }
-keep class * extends com.google.gson.reflect.TypeToken
-keep public class * extends com.google.gson.reflect.TypeToken

# Ensure @SerializedName annotations are not stripped
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

# --- ROOM SPECIFIC RULES ---
# Keep the DAOs so Room can find them via reflection
-keep interface com.vsecurity.scanner.data.local.**Dao { *; }