/**
 * 
 */
package edu.leti.jbpm.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.h2.jdbcx.JdbcConnectionPool;
import org.hibernate.HibernateException;
import org.hibernate.cfg.Environment;
import org.hibernate.connection.ConnectionProvider;

/**
 * @author eav 2011
 */
public class H2Connector implements ConnectionProvider {
    @SuppressWarnings( "unused" )
    private static final Logger log = Logger.getLogger( H2Connector.class );

    protected JdbcConnectionPool connectionPool;

    @Override
    public void close() throws HibernateException {
        if ( connectionPool != null ) {
            connectionPool.dispose();
            connectionPool = null;
        }
    }

    @Override
    public void closeConnection( final Connection conn ) throws SQLException {
        conn.close();
    }

    @Override
    public void configure( final Properties props ) throws HibernateException {
        final String url = props.getProperty( Environment.URL );
        final String user = props.getProperty( Environment.USER );
        final String pass = props.getProperty( Environment.PASS );
        connectionPool = JdbcConnectionPool.create( url, user, pass );
        connectionPool.setMaxConnections( 5 );
    }

    @Override
    public Connection getConnection() throws SQLException {
        return connectionPool.getConnection();
    }

    @Override
    public boolean supportsAggressiveRelease() {
        return false;
    }
}
