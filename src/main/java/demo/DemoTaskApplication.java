package demo;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.task.configuration.EnableTask;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
@EnableTask
public class DemoTaskApplication {
	
	@Autowired
    JdbcTemplate jdbcTemplate;
	
	public static void main(String[] args) {
		SpringApplication.run(DemoTaskApplication.class, args);
	}
	
	@Bean
	public EtlTask etlTask() {
		return new EtlTask();
	}
	
	public class EtlTask implements CommandLineRunner {

		@Override
		public void run(String... strings) throws Exception {
			List<String[]> nameInfo = new ArrayList<String[]>();
			// get names
	        jdbcTemplate.query(
	                "SELECT name FROM names", new Object[] {  },
	                (rs, rowNum) -> new String(rs.getString(1))
	        ).forEach(n -> nameInfo.add(new String[] { n, reverse(n) }));

	        jdbcTemplate.execute("TRUNCATE TABLE emans");

	        String sql = "INSERT INTO emans(name,eman) VALUES (?,?)";
	        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
	        	@Override
	        	public int getBatchSize() {
	        		return nameInfo.size();
	        	}

				@Override
				public void setValues(PreparedStatement ps, int x) throws SQLException {
					String[] vals = nameInfo.get(x);
					ps.setString(1, vals[0]);
					ps.setString(2, vals[1]);
				}
	          });
		}
		
	}

	private String reverse(String n) {
		// TODO Auto-generated method stub
		return new StringBuffer(n).reverse().toString();
	}

}
