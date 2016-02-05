/*UPDATE policydefs set form='{
  "type": "object",
  "title": "Rate Limiting",
  "properties": {
    "day": {
      "title": "Day(s)",
      "description": "The amount of HTTP requests the developer can make per day. At least one limit must exist.",
      "pattern": "^[1-9][0-9]*$",
      "type": "string"
    },
    "minute": {
      "title": "Minute(s)",
      "description": "The amount of HTTP requests the developer can make per minute. At least one limit must exist.",
      "pattern": "^[1-9][0-9]*$",
      "type": "string"
    },
    "second": {
      "title": "Second(s)",
      "description": "The amount of HTTP requests the developer can make per second. At least one limit must exist.",
      "pattern": "^[1-9][0-9]*$",
      "type": "string"
    },
    "hour": {
      "title": "Hour(s)",
      "description": "The amount of HTTP requests the developer can make per hour. At least one limit must exist.",
      "pattern": "^[1-9][0-9]*$",
      "type": "string"
    },
    "month": {
      "title": "Month(s)",
      "description": "The amount of HTTP requests the developer can make per month. At least one limit must exist.",
      "pattern": "^[1-9][0-9]*$",
      "type": "string"
    },
    "year": {
      "title": "Year(s)",
      "description": "The amount of HTTP requests the developer can make per year. At least one limit must exist.",
      "pattern": "^[1-9][0-9]*$",
      "type": "string"
    }
  }
}' WHERE id = 'RateLimiting';*/

--update policydefs set scope_service=FALSE where id='BasicAuthentication';

--update policydefs set scope_service=FALSE where id='SSL';

--update policydefs set scope_service=FALSE where id='FileLog';

--update policydefs set scope_service=FALSE where id='KeyAuthentication';

--ALTER TABLE users ADD COLUMN kong_username VARCHAR(255);



-- INSERT INTO memberships (id,created_on, org_id, role_id, user_id) VALUES (1011,CURRENT_DATE,'Rombit','Owner','ex02394');
-- INSERT INTO memberships (id,created_on, org_id, role_id, user_id) VALUES (1011,CURRENT_DATE,'Trust1Team','Owner','maartens');
