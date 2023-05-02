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
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("org.projectlombok:lombok:1.18.26")
	runtimeOnly("com.h2database:h2")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	implementation("org.springframework.boot:spring-boot-starter-security")
	testImplementation("org.springframework.security:spring-security-test:6.0.2")
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
	runtimeOnly("org.postgresql:postgresql:42.5.4")
	compileOnly("org.projectlombok:lombok:1.18.26")

}

tasks.withType<Test> {
	useJUnitPlatform()
}
