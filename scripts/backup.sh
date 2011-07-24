java -cp ../jpa-training/lib/h2-1.3.154.jar org.h2.tools.Script \
     -url jdbc:h2:../db-storage/users -user sa \
     -script backup.zip -options DROP compression zip 
