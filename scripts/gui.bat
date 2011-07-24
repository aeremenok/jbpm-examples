@start javaw -cp "h2-1.3.154.jar;%H2DRIVERS%;%CLASSPATH%" org.h2.tools.Console -url jdbc:h2:../db-storage/users -user sa
@if errorlevel 1 pause
