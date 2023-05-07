plugins {
	java
	id("org.springframework.boot") version "3.0.5"
	id("io.spring.dependency-management") version "1.1.0"
	id("io.freefair.lombok") version "8.0.1"
}

group = "uj.wmii.jwzp"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
	mavenCentral()
	maven {
		url = uri("https://repo.clojars.org")
		name = "Clojars"
	}
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-jdbc")

    implementation("org.projectlombok:lombok:1.18.26")
	implementation("org.springframework.boot:spring-boot-starter-validation:3.0.4")
	implementation("org.jetbrains:annotations:24.0.1")
	runtimeOnly("com.h2database:h2")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test:6.0.2")
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
	runtimeOnly("org.postgresql:postgresql:42.5.4")
	compileOnly("org.projectlombok:lombok:1.18.26")

	implementation ("org.springframework.security:spring-security-core:6.0.2")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation ("org.springframework.security:spring-security-config")
	compileOnly("org.springframework.security:spring-security-oauth2-jose")
	implementation ("org.springframework.security:spring-security-oauth2-resource-server")


	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

	implementation("org.apache.httpcomponents:httpclient:4.5.13")





}

tasks.withType<Test> {
	useJUnitPlatform()
}
