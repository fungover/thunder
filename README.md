<h1 align="center">
    <img src="https://www.iths.se/wp-content/uploads/2016/02/ithslogoliggandeposrgb-1024x207.png" height="130" alt="ITHS">
</h1>

# ⚡ Thunder ⚡

<section>
    <a href="https://github.com/fungover/thunder/issues">
        <img src="https://img.shields.io/github/issues-raw/fungover/thunder" alt="Open issues workflow badge"/>
    </a>
    <a href="https://github.com/fungover/thunder/pulls">
        <img src="https://img.shields.io/github/issues-pr/fungover/thunder" alt="Pull request workflow badge"/>
    </a>
    <a href="https://github.com/fungover/thunder/issues?q=is%3Aissue+is%3Aclosed">
        <img src="https://img.shields.io/github/issues-closed-raw/fungover/thunder" alt="Open issues workflow badge"/>
    </a>
    <a href="https://github.com/fungover/thunder/graphs/contributors">
        <img src="https://img.shields.io/github/contributors/fungover/thunder" alt="Contributors workflow badge"/>
    </a>
    <img src="https://img.shields.io/github/languages/top/fungover/thunder" alt="Language workflow badge"/>
</section>



## Description
Leveraging MQTT and Shelly smart home devices to seamlessly control a lamp's on/off functionality within a smart home setup.

## Installation
+ Install IoT app for Smart Home based on MQTT protocol, such as __[IoT MQTT Panel](https://play.google.com/store/apps/details?id=snr.lab.iotmqttpanel.prod&hl=sv&gl=DE)__.
+ Or use __[MQTT Explorer](https://mqtt-explorer.com/)__, an MQTT client that provides a structured overview of your MQTT topics.

## Instruction 
...needs to be updated later on in the project...

## Usage 
...needs to be updated later on in the project..
"Use this space to show useful examples of how a project can be used. Additional screenshots, code examples and demos work well in this space."

## Licensing
This software is licensed under the MIT License, allowing you to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the software under the following conditions:
+ Include the original copyright message and the MIT License in all copies or substantial portions of the software.
For more details, refer to the [MIT License](LICENSE) file.

## Running the Application with Docker

Follow these steps to run the application using Docker.

#### Step 1: Pull the Docker Image

\`\`\`
docker pull thunder:latest
\`\`\`
#### Step 2: Run the Docker Container

\`\`\`
docker run -p 1883:1883 -d thunder:latest
\`\`\`