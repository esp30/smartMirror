# Data
#### Users
- Id (Primary Key)
- Name
- Age
- ....

#### Emotions
- Id (Primary Key)
- Timestamp
- User
- Description / Emoji 
- ...

# Rest API

#### /users
- Returns a list with all user ids

#### /user{id}
- Returns information about a particular user

#### /emotions{id}{hist=10}
- Returns history of emotions about a particular user