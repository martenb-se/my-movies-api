# File should only be sourced (thanks https://stackoverflow.com/a/28776166)
sourced=0
if [ -n "$ZSH_VERSION" ]; then
  case $ZSH_EVAL_CONTEXT in *:file) sourced=1;; esac
elif [ -n "$KSH_VERSION" ]; then
  [ "$(cd -- "$(dirname -- "$0")" && pwd -P)/$(basename -- "$0")" != "$(cd -- "$(dirname -- "${.sh.file}")" && pwd -P)/$(basename -- "${.sh.file}")" ] && sourced=1
elif [ -n "$BASH_VERSION" ]; then
  (return 0 2>/dev/null) && sourced=1
else # All other shells: examine $0 for known shell binary filenames.
     # Detects `sh` and `dash`; add additional shell filenames as needed.
  case ${0##*/} in sh|-sh|dash|-dash) sourced=1;; esac
fi

if [[ $sourced = 0 ]]; then
  echo "File should not be run directly, it should only be sourced.";
  exit 1;
fi

# Reset all variables
unset DEV_DOCKER_CONTAINER_NAME
unset DEV_API_PORT
unset API_PORT
unset SPRING_APPLICATION_NAME
unset LOGGING_LEVEL
unset SPRING_DATASOURCE_URL_HOST
unset SPRING_DATASOURCE_URL_PORT
unset SPRING_DATASOURCE_URL_DB
unset SPRING_DATASOURCE_USERNAME
unset SPRING_DATASOURCE_PASSWORD
# - Testing Variables
unset SPRING_TESTING_PROFILES_ACTIVE

# Verify .env file exists
if [ ! -f .env ]; then
  echo "Please create the .env file with all necessary variables before running script";
  exit 1;
fi

# Export env vars
export $(grep -v '^#' .env | xargs)

# Verify env vars
if [ -z ${DEV_DOCKER_CONTAINER_NAME} ] || [ -v "$DEV_DOCKER_CONTAINER_NAME" ]; then
  echo "Please set \$DEV_DOCKER_CONTAINER_NAME in the .env file";
  exit 1;
fi

if [ -z ${DEV_API_PORT} ] || [ -v "$DEV_API_PORT" ]; then
  echo "Please set \$DEV_API_PORT in the .env file";
  exit 1;
fi

if [ -z ${SPRING_APPLICATION_NAME} ] || [ -v "$SPRING_APPLICATION_NAME" ]; then
  echo "Please set \$SPRING_APPLICATION_NAME in the .env file";
  exit 1;
fi

if [ -z ${LOGGING_LEVEL} ] || [ -v "$LOGGING_LEVEL" ]; then
  echo "Please set \$LOGGING_LEVEL in the .env file";
  exit 1;
fi

if [ -z ${SPRING_DATASOURCE_URL_DB} ] || [ -v "$SPRING_DATASOURCE_URL_DB" ]; then
  echo "Please set \$SPRING_DATASOURCE_URL_DB in the .env file";
  exit 1;
fi

if [ -z ${SPRING_DATASOURCE_USERNAME} ] || [ -v "$SPRING_DATASOURCE_USERNAME" ]; then
  echo "Please set \$SPRING_DATASOURCE_USERNAME in the .env file";
  exit 1;
fi

if [ -z ${SPRING_DATASOURCE_PASSWORD} ] || [ -v "$SPRING_DATASOURCE_PASSWORD" ]; then
  echo "Please set \$SPRING_DATASOURCE_PASSWORD in the .env file";
  exit 1;
fi

if ! [[ $DEV_API_PORT =~ ^[0-9]+$ ]]; then
  echo "Please set \$DEV_API_PORT to a NUMERIC value only, not '$DEV_API_PORT'";
  exit 1;
fi

if ! [[ $LOGGING_LEVEL =~ ^(TRACE|DEBUG|INFO|WARN|ERROR|FATAL|OFF)$ ]]; then
  echo "Please set \$LOGGING_LEVEL to either of TRACE, DEBUG, INFO, WARN, ERROR, FATAL or OFF only, not '$LOGGING_LEVEL'";
  exit 1;
fi

if [ -n "$API_PORT" ] && ! [[ $API_PORT =~ ^[0-9]+$ ]]; then
  echo "If using \$API_PORT for development, please set is to a NUMERIC value only, not '$API_PORT'";
  exit 1;
fi

if [ -n "$SPRING_DATASOURCE_URL_PORT" ] && ! [[ $SPRING_DATASOURCE_URL_PORT =~ ^[0-9]+$ ]]; then
  echo "If using \$SPRING_DATASOURCE_URL_PORT for development, please set is to a NUMERIC value only, not '$SPRING_DATASOURCE_URL_PORT'";
  exit 1;
fi
