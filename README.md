oauth2-example
==============

Example of an OAuth2 client for Google.

## Prepare

Get `client_id` and `client_secret` by setting up a project at <https://cloud.google.com/console/project>. The app tries to retrieve user info so the Google+ API should be active. The redirect URI should be <https://localhost/oauth2/callback>.

## Build

      mvn clean package

## Run

      java -jar target/oauth2-example-0.1.0-SNAPSHOT.one-jar.jar <client_id> <client_secret>

## Use

1. Browse to <https://localhost:8443/>.
2. Click the sign in button.
3. Approve the permissions requested by the app.

