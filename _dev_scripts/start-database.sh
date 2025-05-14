#!/bin/bash

# Variables
DB_NAME="nexalyst"
DB_USER="nexalyst_user"
DB_PASSWORD="nexalyst_default_password"

# Create the database
psql -d postgres -v ON_ERROR_STOP=1 <<EOF
CREATE DATABASE $DB_NAME;
EOF

# Create the user with an encrypted password
psql -d postgres -v ON_ERROR_STOP=1 <<EOF
CREATE USER $DB_USER WITH ENCRYPTED PASSWORD '$DB_PASSWORD';
EOF

# Grant the user access to the database
psql -d postgres -v ON_ERROR_STOP=1 <<EOF
GRANT ALL PRIVILEGES ON DATABASE $DB_NAME TO $DB_USER;
EOF

echo "Database '$DB_NAME' and user '$DB_USER' created successfully with access granted."