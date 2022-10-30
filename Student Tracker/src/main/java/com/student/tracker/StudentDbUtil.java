package com.student.tracker;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

public class StudentDbUtil {
private DataSource dataSource;

public StudentDbUtil(DataSource theDataSource) {
	this.dataSource = theDataSource;
   }

	public List<Student> getStudents() throws Exception{
		List<Student> students = new ArrayList<>();
		
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		
		try {
			con = dataSource.getConnection();
			st = con.createStatement();
			String sql = "select * from student order by last_name";
			rs = st.executeQuery(sql);
			
			while(rs.next()) {
			  int id = rs.getInt(1);
			  String firstName = rs.getString(2);
			  String lastName = rs.getString(3);
			  String email = rs.getString(4);
			  
			  Student temp = new Student(id,firstName,lastName,email);
			  students.add(temp);
		}
			return students;
	}
		finally {
			close(con,st,rs);
			
		}
		
		
}

	private void close(Connection con, Statement st, ResultSet rs) {
		try {
			if(rs!=null) {
				rs.close();
			}
			if(st!=null) {
				st.close();
			}
			if(con!=null) {
				con.close();
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}

	public void addStudent(Student newStudent) throws Exception {
		
		Connection con = null;
		PreparedStatement ps= null;
		
		try {
			con = dataSource.getConnection();
			
			String sql = "insert into student "+"(first_name,last_name,email)"+"values (?,?,?)";
			
			ps = con.prepareStatement(sql);
			ps.setString(1, newStudent.getFirstName());
			ps.setString(2, newStudent.getLastName());
			ps.setString(3, newStudent.getEmail());
			ps.execute();
			
		}
		
		finally {
			close(con,ps,null);
		}
		
	}

	public Student getStudents(String id) throws Exception {
		Student theStudent =null;
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int studentId;
		
		try {
		studentId = Integer.parseInt(id);
		
		con = dataSource.getConnection();
		
		String sql = "select * from student where id=?";
		ps = con.prepareStatement(sql);
		ps.setInt(1, studentId);
		
		rs = ps.executeQuery();
		
		if(rs.next()) {
			String firstName = rs.getString("first_name");
			String lastName = rs.getString("last_name");
			String email = rs.getString("email");
			
			theStudent = new Student(studentId,firstName,lastName,email);
		}
		else {
			throw new Exception("Could not find student id: "+studentId);
		}
			return theStudent;
		}
		
		finally {
			close(con,ps,rs);
		}
		
		
		
	}

	public void updateStudent(Student newStudent) throws Exception {
		Connection con = null;
		PreparedStatement ps = null;
		
		
		try {
			con = dataSource.getConnection();
			
			String sql = "update student "+"set first_name=?,last_name=?,email=? "+"where id=?";
			
			ps = con.prepareStatement(sql);
			ps.setString(1, newStudent.getFirstName());
			ps.setString(2, newStudent.getLastName());
			ps.setString(3, newStudent.getEmail());
			ps.setInt(4, newStudent.getId());
			ps.execute();
			
		}
		
		finally {
			close(con,ps,null);
		
	}

	}

	public void deleteStudent(String id) throws Exception {
		Connection con = null;
		PreparedStatement ps = null;
		int studentId;
		
		try {
			studentId = Integer.parseInt(id);
			
			con = dataSource.getConnection();
			
			String sql = "delete from student where id=?";
			ps = con.prepareStatement(sql);
			ps.setInt(1, studentId);
			ps.execute();
			}
			
			finally {
				close(con,ps,null);
			}
		
		
	}
	
}
