# OJA (OpenAI Java API)

[version]: https://img.shields.io/github/v/tag/lookforfps/oja?label=Version
[wiki-docs]: https://img.shields.io/badge/Wiki-Docs-%2368CBB0
[licence]: https://img.shields.io/github/license/lookforfps/oja?label=Licence&color=%23fff

[wiki-docs-link]: https://oja.lookforfps.dev/introduction/oja/
[licence-link]: https://github.com/LookforFPS/OJA/blob/main/LICENSE.md

[![version][]]()
[![wiki-docs][]][wiki-docs-link]
[![licence][]][licence-link]

OJA is an open-source Java library designed to simplify the integration of OpenAI's API into Java environments.
It allows developers to easily use OpenAI's AI models for tasks like chat completions and embeddings.

## Warning
**As this is a early version of OJA, there may be bugs and other issues that could affect performance and functionality.**<br>
This version only supports chat completion and embeddings.<br>
Future updates will introduce additional functionalities, including the implementation of features such as Image Generation and Text Moderation.

## Installation

Since OJA is currently not available on Maven Central, you will need to specify a custom repository.
Apart from that, OJA can be integrated into your software just like any other package using **Gradle** or **Maven**.

### Maven
```xml
<repository>
    <id>lookforfps</id>
    <url>https://repo.lookforfps.dev/repository/maven-public</url>
</repository>
```

```xml
<dependency>
    <groupId>me.lookforfps</groupId>
    <artifactId>OJA</artifactId>
    <version>version</version>
</dependency>
```

### Gradle
```groovy
repositories {
    maven {
        url "https://repo.lookforfps.dev/repository/maven-public"
    }
}
```

```groovy
dependencies {
    implementation("me.lookforfps:OJA:version")
}
```

## Get Started

### Chat Completion
First, we build a `ChatCompletionService` according to our needs.
Next, we add a system message with some instructions and a user message to the context.
After that, we send the request to OpenAI.
```java
ChatCompletionService service = ChatCompletionService.build("put$your#api%token§here", ChatCompletionModel.GPT_4_O_MINI.getIdentifier());

service.addMessage(new SystemMessage("You are an Assistant named Sarah."));
service.addMessage(new UserMessage("What is the biggest City in the world?"));

ChatCompletionResponse response = service.sendRequest();
```

## Documentation

Due to the extensive range of core features that we will be offering, providing a detailed documentation within the repository's README alone is not possible.
To ensure that each feature is completely and clearly explained, we have created a [dedicated wiki page](https://oja.lookforfps.dev).
This resource will provide detailed and structured explanations for all features, allowing for a more in-depth understanding and easier navigation.

## Licence

OJA is licensed under the **MIT License**. For more information, see the [`LICENSE.md`][licence-link] file in the project repository.