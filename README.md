# OJA (OpenAI Java API)

This open source library is intending to streamline the integration of OpenAI's API in Java environments.
It aims to simplify the process for developers, enabling them to utilize OpenAI's AI models for a variety of tasks.

## Warning
**As this is a `PREVIEW` version, there may be bugs and other issues that could affect performance and functionality.**<br>
This version only supports chat completion, allowing responses to be received either as a whole or streamed in chunks with already processed words.<br>
<br>
**The functionality to use functions and tools in chat completion is not implemented yet.**<br>
<br>
Future updates will introduce additional functionalities, including the implementation of features such as Image Generation and Audio Transcription.

## Get Started

### Create ChatCompletion

> The `ChatCompletion` object created by the `OJABuilder` can be used to add messages to the context and send requests to OpenAI.
It is intended for reuse, as it maintains the chat context, which is included in each request.

The OJABuilder offers several options to create a ChatCompletion object, depending on the information provided.

#### Basic (API Token and Model)

Specify the OpenAI API token and define the model using the ChatCompletionModel enum.
```java
ChatCompletion chatCompletion = OJABuilder.createChatCompletion("put$your#api%token§here", ChatCompletionModel.GPT_3_5_TURBO.getAIModel());
```
Or... if you want to use a specify model, you can provide a string with the model identifier.
```java
ChatCompletion chatCompletion = OJABuilder.createChatCompletion("put$your#api%token§here", "gpt-3.5-turbo-1106");
```

#### Advanced (API Token, Model and Configuration)

Create a new chat completion configuration to set properties such as `maxToken` and `serviceTier`.<br>
Specify the OpenAI API token, define the model using the ChatCompletionModel enum or model identifier string, and provide the necessary configuration.
```java
ChatCompletionConfiguration config = new ChatCompletionConfiguration();
config.setMaxTokens(1000);
config.setServiceTier("default");

ChatCompletion chatCompletion = OJABuilder.createChatCompletion("put$your#api%token§here", ChatCompletionModel.GPT_3_5_TURBO.getAIModel(), config);
```

---

### Adding a Message to Context

> Every message has a role that indicates the perspective from which the AI should interpret it.<br>
> There are 5 roles available for messages:
> - `user` A standard message from the user.
> - `assistent` A message generated by the AI.
> - `system` Typically used to provide instructions to the AI.
> - `tool` Intended for providing tools to the AI **(not yet implemented)**.
> - `function` Intended for providing functions to the AI **(deprecated and not implemented)**.

#### TextMessage

Simply provide the text you want to add to the context.<br>
*The role will be `user` by default*
```java
chatCompletion.addTextMessage("yourMessage");
```
You also have the option to specify a role for your TextMessage.
```java
chatCompletion.addTextMessage("system", "yourMessage");
```
Or... you can use the `MessageRole` enum to specify the role of your TextMessage.
```java
chatCompletion.addTextMessage(MessageRole.SYSTEM.getIdentifier(), "yourMessage");
```

#### ImageMessage
> Image inputs are currently supported exclusively by GPT-4 and newer models.

Specify the `imageUrl` of the image you want to add the context.<br>
*The role will be `user` by default*
```java
chatCompletion.addImageMessage("https://here.is.my/image.png");
```
You also have the option to specify a role for your ImageMessage. Here you can also use the MessageRole enum.
```java
chatCompletion.addImageMessage("system", "https://here.is.my/image.png");
```
And... you can also include additional text to provide context or describe the image, for example.
```java
chatCompletion.addImageMessage("system", "https://here.is.my/image.png", "This is my unicorn. It's pretty right?");
```

---

### Use Tools and Function
> Tools and their functions enable the AI to utilize specific resources when needed.
> For example, if someone asks for the weather, a designated tool can be called to provide the information, including location and unit, allowing you to respond appropriately.
> <br>**When a tool gets called, no message will be generated.**

> If you prefer, you can prepare several lists of tools and use the setter method to replace the tools in the configuration all at once.
#### Add Tools to Configuration

Tools must be added to the configuration. They are sent with each request, making them available whenever needed.
```java
configuration.addTool(new Tool(
                ToolType.FUNCTION, // type of tool
                new Function(
                        "get_current_weather", // name of function
                        "Get the current weather in a given location", // description of function
                        new Parameters(
                                ParameterType.OBJECT, // type of parameter
                                new String[]{"location"}, // required properties
                                Property.create(
                                        "location", // name of property
                                        PropertyType.STRING, // type of property
                                        "The city and state, e.g. San Francisco, CA" // description of property
                                        ),
                                Property.create(
                                        "unit", // name of property
                                        PropertyType.STRING, // type of property
                                        new String[] {"celsius", "fahrenheit"} // enum values for property
                                )
                        )
                )
        ));
```

#### Remove Tools from Configuration

Tools can also be removed from the configuration by object reference.
```java
configuration.removeTool(tool);
```
Or... by the index number.
```java
configuration.removeTool(3);
```

#### Set ToolChoice in Configuration
> The tool choice setting determines which tools the AI can call:
> - `none` The model will not call any tools and will generate a message instead.
> - `auto` The model can choose between generating a message or calling one or more tools.
> - `required` The model must call one or more tools.

To set the tool choice, you can use the ToolChoice enum.
```java
configuration.setToolChoice(ToolChoice.REQUIRED.getIdentifier());
```
Or... specify a custom tool choice in JSON format.
```java
configuration.setToolChoice("{\"type\": \"function\", \"function\": {\"name\": \"get_current_weather\"}}");
```

---

### Send Synchronous Request

> All `addMessage` methods of the `ChatCompletion` class are designed to return the ChatCompletion object, allowing you to send a request directly with the returned value.

Just use the `sendRequest` method.
```java
ChatCompletionResponse response = chatCompletion.sendRequest();
```
Or... you can send the request by adding a message.
```java
chatCompletion.addTextMessage("yourMessage").sendRequest();
```

---

### Streaming

#### Send Request

Use the `sendStreamRequest` method to obtain a Stream object.
```java
Stream stream = chatCompletion.sendStreamRequest();
```

#### Handle Stream Events

Just use the `addStreamListener` method to add a StreamListener to Stream.
```java
stream.addStreamListener(new StreamListener() {
    @Override
    public void onChunkStreamed(ChunkStreamedEvent event) {
        // Handle chunk streamed event
    }

    @Override
    public void onStreamStopped() {
        // Handle stream stopped event
    }
});
```
Or... you can add a StreamListener on `sendStreamRequest` method.
```java
Stream stream = chatCompletion.sendStreamRequest(new StreamListener() {
    @Override
    public void onChunkStreamed(ChunkStreamedEvent event) {
        // Handle chunk streamed event
    }

    @Override
    public void onStreamStopped() {
        // Handle stream stopped event
    }
});
```

---

## Licence

OJA is licensed under the **MIT License**. For more information, see the `LICENSE.md` file in the project repository.