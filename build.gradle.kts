plugins {
  id("java")
  id("com.apollographql.apollo").version("2.5.13")
}

repositories {
  mavenCentral()
}
dependencies {
  add("implementation", "com.apollographql.apollo:apollo-runtime:2.5.13")
  add("testImplementation", "junit:junit:4.12")
  add("testImplementation", "com.squareup.okhttp3:mockwebserver:4.10.0")
}