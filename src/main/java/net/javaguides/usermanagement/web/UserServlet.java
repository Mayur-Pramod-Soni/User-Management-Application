package net.javaguides.usermanagement.web;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.javaguides.usermanagement.dao.UserDao;
import net.javaguides.usermanagement.models.User;

/**
 * Servlet implementation class UserServlet
 */
@WebServlet("/")
public class UserServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
    private UserDao userDao ;
    
    //Constructor
//    public UserServlet() {
////        super();
//        // TODO Auto-generated constructor stub
//    }
    
    public void init()
    {
    	userDao = new UserDao();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String action  = request.getServletPath();
		
		try {
			switch (action) {
			case "/new":
				showNewForm(request,response);
				break;
			case "/insert":
				insertUser(request,response);
				break;
			case "/delete":
				deleteUser(request,response);
				break;
			case "/edit":
				showEditForm(request,response);
				break;
			case "/update":
				updateUser(request,response);
				break;
			default:
				listUser(request,response);
				break;
			}
		}
		catch(SQLException e)
		{
			throw new ServletException(e);			
		}
	}
	
	private void listUser(HttpServletRequest request,HttpServletResponse response)throws SQLException,IOException, ServletException
	{
		List<User> listUser = userDao.selectAllUsers();
		request.setAttribute("listUser", listUser);
		RequestDispatcher dispatcher = request.getRequestDispatcher("user-list.jsp");
		dispatcher.forward(request, response);
	}
	
	private void showNewForm(HttpServletRequest request,HttpServletResponse response)throws ServletException,IOException
	{
		RequestDispatcher dispatcher = request.getRequestDispatcher("user-form.jsp");
		dispatcher.forward(request, response);
	}
	
	private void showEditForm(HttpServletRequest request , HttpServletResponse response )throws IOException, SQLException, ServletException
	{
		int id  = Integer.parseInt(request.getParameter("id"));
		User existingUser = userDao.selectUser(id);
		RequestDispatcher dispatcher = request.getRequestDispatcher("user-form.jsp");
		request.setAttribute("user", existingUser);
		dispatcher.forward(request, response);
	}
	
	private void insertUser(HttpServletRequest request , HttpServletResponse response)throws IOException, SQLException, ServletException
	{
		String name  = request.getParameter("name");
		String email  = request.getParameter("email");
		String country  = request.getParameter("country");
		
		User newUser = new User(name,email,country);
		
		userDao.insertUser(newUser);
		response.sendRedirect("list");
	}
	
	private void updateUser(HttpServletRequest request , HttpServletResponse response)throws IOException, SQLException, ServletException
	{
		int id = Integer.parseInt(request.getParameter("id"));
		String name = request.getParameter("name");
		String email = request.getParameter("email");
		String country = request.getParameter("country");
		
		User updateUserData = new User(id , name, email, country);
		userDao.updateUser(updateUserData);
		response.sendRedirect("list");
	}
	
	private void deleteUser(HttpServletRequest request , HttpServletResponse response)throws IOException, SQLException, ServletException
	{
		int id  = Integer.parseInt(request.getParameter("id"));
		userDao.deleteUser(id);
		response.sendRedirect("list");
	}
}




