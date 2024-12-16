
# MallApp Superapp

Welcome to the MallApp REST API documentation! 
This API serves as the backend for the SuperApp, a powerful and versatile application that provides various services and features. This document provides an overview of the API endpoints and their functionalities.

## Endpoints

### Users

#### Create a new user

```http
  POST /superapp/users
```

#### Get a user

```http
  GET /superapp/users/login/{superapp}/{email}
```

| Variable | Type     | Description          |
| :-------- | :------- | :------------------ |
| `superapp` | `string` | Your superapp name |
| `email` | `string` | The wanted email      |

#### Update user details

```http
  PUT /superapp/users/{superapp}/{userEmail}
```
    Requiers a JSON body

| Variable | Type     | Description              |
| :-------- | :------- | :---------------------- |
| `superapp` | `string` | Your superapp name     |
| `userEmail` | `string` | The email of the user |

### Objects

#### Create an object
```http
  POST /superapp/objects
```
#### update an object
```http
  PUT /superapp/objects/{superApp}/{internalObjectId}
```

    Requiers a JSON body
| Variable | Type     | Description                        |
| :-------- | :------- | :-------------------------------- |
| `superapp` | `string` | Your superapp name               |
| `internalObjectId` | `string` | The id of the object     |

#### Retrieve an object
```http
  GET /superapp/objects/{superApp}/{internalObjectId}
```

| Variable | Type     | Description                       |
| :-------- | :------- | :------------------------------- |
| `superapp` | `string` | Your superapp name              |
| `internalObjectId` | `string` | The id of the object    |

#### Get All objects
```http
  GET /superapp/objects
```

  | Variable | Type |Description           |
  | :-------- | :------- | :-------------- |
  | `type` | `string` | The requested type |
  | Parameter | |                          |
  |`superapp`| `string`|                   |
  |`email`| `string`|                      |
  |`size`| `int`|                          |
  |`page`| `int`|                          |
  
  * By their type
  ```http
  GET /superapp/objects/search/byType/{type}
  ```

  | Variable | Type     | Description      |
  | :-------- | :------- | :-------------- |
  | `type` | `string` | The requested type |
  | Parameter | |                          |
  |`superapp`| `string`|                   |
  |`email`| `string`|                      |
  |`size`| `int`|                          |
  |`page`| `int`|                          |
  
  * By their alias
  ```http
  GET /superapp/objects/search/byAlias/{alias}
  ```

  | Variable | Type     | Description        |
  | :-------- | :------- | :---------------- |
  | `alias` | `string` | The requested alias |
  | Parameter | |                            |
  |`superapp`| `string`|                     |
  |`email`| `string`|                        |
  |`size`| `int`|                            |
  |`page`| `int`|                            |
  
  * By their loaction
  ```http
  POST /superapp/objects/search/byLocation/{lat}/{lng}/{distance}
  ```
  
  | Variable | Type     | Description                     |
  | :-------- | :------- | :----------------------------- |
  | `lat` | `string` | The current latitude               |
  | `lng` | `string` | The current longitude              |
  | `distance` | `string` | The distance to search inside |
  | Parameter | |                                         |
  |`distanceUnits`| `string`|                             |
  |`superapp`| `string`|                                  |
  |`email`| `string`|                                     |
  |`size`| `int`|                                         |
  |`page`| `int`|                                         |
  

#### Bind an existing object to a child object
```http
  PUT /superapp/objects/{superApp}/{InternalObjectId}/children
```

    Requiers a JSON body
  | Variable | Type     | Description                   |
  | :-------- | :------- | :--------------------------- |
  | `superapp` | `string` | Your superapp name          |
  | `originId` | `string` | The id of the parent object |

#### Get all children objects
```http
  GET /superapp/objects/{superApp}/{InternalObjectId}/children
```

| Variable | Type     | Description                   |
| :-------- | :------- | :--------------------------- |
| `superapp` | `string` | Your superapp name          |
| `originId` | `string` | The id of the parent object |
| Parameter | |                                       |
|`userSuperapp`| `string`|                            |
|`email`| `string`|                                   |
|`size`| `int`|                                       |
|`page`| `int`|                                       |


#### Get all parents objects by child
```http
  GET /superapp/objects/{superApp}/{InternalObjectId}/parents
```

| Variable | Type     | Description                     |
| :-------- | :------- | :----------------------------- |
| `superapp` | `string` | Your superapp name            |
| `childrenId` | `string` | The id of the parent object |
| Parameter | |                                         |
|`userSuperapp`| `string`|                              |
|`email`| `string`|                                     |
|`size`| `int`|                                         |
|`page`| `int`|                                         |

### Commands

#### Invoke a miniapp command
```http
  POST /superapp/miniapp/{miniAppName}
```

  | Variable | Type |Description                              |
  | :-------- | :------- | :--------------------------------  |
  | `miniAppName` | `string` | The miniapp in which to invoke |
  | Parameter |              |                                |
  |`async`| `boolean` |                                       |
  
### Admin

#### Delete all users
```http
  DELETE /superapp/admin/users
```

  | Parameter | Type |Description                               |
  | :-------- | :------- | :--------------------------------    |
  |`userSuperapp`| `string`| The superapp                       |
  |`userEmail`| `string`| Email of the user trying to delete all|

#### Delete all objects
```http
  DELETE /superapp/admin/objects
```

  | Parameter | Type |Description                               |
  | :-------- | :------- | :----------------------------------- |
  |`userSuperapp`| `string`| The superapp                       |
  |`userEmail`| `string`| Email of the user trying to delete all|

#### Delete all commands history
```http
  DELETE /superapp/admin/miniapp
```

  | Parameter | Type |Description                               |
  | :-------- | :------- | :----------------------------------- |
  |`userSuperapp`| `string`| The superapp                       |
  |`userEmail`| `string`| Email of the user trying to delete all|

#### Get all users
```http
  GET /superapp/admin/users
```

  | Parameter | Type |Description                            |
  | :-------- | :------- | :-------------------------------- |
  |`userSuperapp`| `string`| The superapp                    |
  |`userEmail`| `string`| Email of the user trying to get all|
  |`size`| `int`|                                            |
  |`page`| `int`|                                            |

#### Get all commands
```http
  GET /superapp/admin/miniapp
```

  | Parameter | Type |Description |
  | :-------- | :------- | :-------------------------------- |
  |`userSuperapp`| `string`| The superapp                    |
  |`userEmail`| `string`| Email of the user trying to get all|
  |`size`| `int`|                                            |
  |`page`| `int`|                                            |

#### Get all commands from a specific miniapp
```http
  GET /superapp/admin/miniapp/{miniAppName}
```

  | Variable | Type |Description |
  | :-------- | :------- | :-------------------------------- |
  |`miniAppName`|`string`| The specific miniapp              |
  | Parameter | | |
  |`userSuperapp`| `string`| The superapp                    |
  |`userEmail`| `string`| Email of the user trying to get all|
  |`size`| `int`|                                            |
  |`page`| `int`|                                            |


## Technologies

**Client:** Android Studio, Python TKinter, Retrofit.

**Server:** Spring using Tomcat and Artemis, MongoDB, Docker.

### RUN SERVER WITH DOCKER

#### Prerequisites:
    Docker: [Installation instructions](https://docs.docker.com/get-docker/)
    Windows Subsystem for Linux 2 (WSL 2): [Installation instructions](https://docs.microsoft.com/en-us/windows/wsl/install-win10)

#### Run Docker Compose
Inside \gradle\wrapper - add gradle-wrapper.jar

If you don't have the gradle-wrapper.jar file in your project, you can follow the steps below to generate it:

	1. Open a terminal or command prompt.
    2. Navigate to your project directory.
    3. Run the following Gradle wrapper command to generate the gradle-wrapper.jar file: gradlew wrapper

In application.properties file, remove the comment for this line:

```shell
  spring.data.mongodb.uri=mongodb://mongoDb:27017/2023bshirzurdb
```

Then to create the server jar file run this command:

```shell
  gradlew build -x test
```
And then in order to run the containers:

```shell
  docker-compose up --build 
```
build flag will build the images of the server and the mongo

## Installation
To install the superapp, follow these steps:

    1. Clone the repository to your spring environment
    
    2. Make sure mongoDB is installed on your PC
    
    3. In application.properties update the server.port to 8083
    
    4. In application.properties update the spring.data.mongodb.port to 27017
    
    5. Run the server and start using the miniapps