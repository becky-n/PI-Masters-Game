# JavaFX application using Proxy API

## To setup the API to access Chat Completions
- This will only work if you have an auckland uni email
- You can get an API key from OpenAI API

- add in the root of the project (i.e., the same level where `pom.xml` is located) a file named `apiproxy.config`

  ```
  email: "UPI@aucklanduni.ac.nz"
  apiKey: "YOUR_KEY"
  ```
  These are your credentials to invoke the APIs. 

  The token credits are charged as follows:
  - 1 token credit per 1 character for Googlel "Standard" Text-to-Speech. 
  - 4 token credit per 1 character for Google "WaveNet" and "Neural2" Text-to-Speech.
  - 1 token credit per 1 character for OpenAI Text-to-Text.
  - 1 token credit per 1 token for OpenAI Chat Completions (as determined by OpenAI, charging both input and output tokens).

## To run the game

## on Mac
`./mvnw clean javafx:run`
## on Windows
`.\mvnw.cmd clean javafx:run`
