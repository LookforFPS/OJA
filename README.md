# OJA (OpenAI Java API)

[version]: https://img.shields.io/github/v/release/lookforfps/oja?label=Version
[licence]: https://img.shields.io/github/license/lookforfps/oja?label=Licence&color=%23fff
[build]: https://img.shields.io/github/actions/workflow/status/lookforfps/oja/validation.yml
[wiki-docs]: https://img.shields.io/badge/Wiki-Docs-%2368CBB0

[build-link]: https://github.com/LookforFPS/OJA/actions/workflows/validation.yml
[licence-link]: https://github.com/LookforFPS/OJA/blob/main/LICENSE.md
[wiki-docs-link]: https://oja.lookforfps.dev/introduction/oja/

[![version][]]()
[![licence][]][licence-link]
[![build][]][build-link]
[![wiki-docs][]][wiki-docs-link]

OJA is an open-source Java library designed to simplify the integration of OpenAI's API into Java environments.
It allows developers to easily use OpenAI's AI models for tasks like chat completions, embeddings and moderation.

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
    <groupId>dev.lookforfps.oja</groupId>
    <artifactId>OJA</artifactId>
    <version>version</version>
</dependency>
```

### Gradle
Groovy DSL

```groovy
repositories {
    maven {
        url "https://repo.lookforfps.dev/repository/maven-public"
    }
}
```
```groovy
dependencies {
    implementation "dev.lookforfps:OJA:version"
}
```

Kotlin DSL

```kotlin
repositories {
    maven {
        url = uri("https://repo.lookforfps.dev/repository/maven-public")
    }
}
```

```kotlin
dependencies {
    implementation("dev.lookforfps:OJA:version")
}
```

Alternatively, you can download the project from the [repository](https://github.com/LookforFPS/OJA) and build it yourself using Gradle.


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
System.out.println("Answer: "+response.getTextContent());
```

### Embeddings
First, we build a `EmbeddingService` according to our needs.
Then we send the request with the content we wish to convert into embeddings.
```java
EmbeddingService service = EmbeddingService.build("put$your#api%token§here", EmbeddingModel.TEXT_EMBEDDING_3_LARGE.getIdentifier());

EmbeddingResponse response = service.sendRequest("something you want to convert");
System.out.println("Embedding: "+response.getFloatEmbedding());
```

### Moderation

First, we build a `ModerationService` according to our needs.
Then we send the request with the content we want to moderate.
```java
ModerationService service = ModerationService.build("put$your#api%token§here", ModerationModel.OMNI_MODERATION_LATEST);

ModerationResponse response = service.sendRequest("content you want to check for moderation rules");
System.out.println("Flagged: "+response.getResult().getFlagged());
```


## Documentation

Due to the extensive range of core features that we will be offering, providing a detailed documentation within the repository's README alone is not possible.
To ensure that each feature is completely and clearly explained, we have created a [dedicated wiki page](https://oja.lookforfps.dev).
This resource will provide detailed and structured explanations for all features, allowing for a more in-depth understanding and easier navigation.

## Licence

OJA is licensed under the **MIT License**. For more information, see the [`LICENSE.md`][licence-link] file in the project repository.