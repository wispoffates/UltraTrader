## Admin ##
Some special notes about administration.

### Logging ###
Logging is currently only enabled with a `mysql` database. This means that if you want
to view the logs, you will need to have a `mysql` database server, and a `mysql` database client
for the time being.

In future releases I will be adding in some commands to help view the logs in-game.

To enable logging you need to got into the `config.yml` file in the plugin's folder
and change `logging.enabled` to true, by default it is set to false.

Then enter your username, password, dbname, database address/port if needed. The database
should be on localhost, there is only a 3 millisecond timeout which should be more than
enough for a local connection, if it is not a local connection, expect many records to not
be written.

The log table will be automatically generated. As this is a beta feature at the moment
be ready to manually drop this table in the future (or it will be automatically dropped)
and refreshed.

Currently to view the data you need a `mysql client`.