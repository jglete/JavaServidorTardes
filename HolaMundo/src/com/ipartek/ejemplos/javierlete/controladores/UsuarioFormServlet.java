package com.ipartek.ejemplos.javierlete.controladores;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ipartek.ejemplos.javierlete.dal.DALException;
import com.ipartek.ejemplos.javierlete.dal.UsuariosDAL;
import com.ipartek.ejemplos.javierlete.tipos.Usuario;

@WebServlet("/usuarioform")
public class UsuarioFormServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String op = request.getParameter("op");
		String nombre = request.getParameter("nombre");
		String pass = request.getParameter("pass");
		String pass2 = request.getParameter("pass2");

		RequestDispatcher rutaListado = request.getRequestDispatcher(UsuarioCRUDServlet.RUTA_SERVLET_LISTADO);
		RequestDispatcher rutaFormulario = request.getRequestDispatcher(UsuarioCRUDServlet.RUTA_FORMULARIO);

		// response.setContentType("text/plain");
		// PrintWriter out = response.getWriter();
		// out.println(op);
		// out.println(nombre);
		// out.println(pass);
		// out.println(pass2);

		if (op == null) {
			rutaListado.forward(request, response);
			return;
		}

		Usuario usuario = new Usuario(nombre, pass);

		ServletContext application = request.getServletContext();
		UsuariosDAL dal = (UsuariosDAL) application.getAttribute("dal");

		switch (op) {
		case "alta":
			if (pass.equals(pass2)) {
				dal.alta(usuario);
				request.getParameterMap().remove("op");
				rutaListado.forward(request, response);
			} else {
				usuario.setErrores("Las contrase�as no coinciden");
				request.setAttribute("usuario", usuario);
				rutaFormulario.forward(request, response);
			}
		case "modificar":
			if (pass.equals(pass2)) {
				try {
					dal.modificar(usuario);
				} catch (DALException de) {
					usuario.setErrores("Usuario no existente");
					request.setAttribute("usuario", usuario);
					rutaFormulario.forward(request, response);
				}
				rutaListado.forward(request, response);
			} else {
				usuario.setErrores("Las contrase�as no coinciden");
				request.setAttribute("usuario", usuario);
				rutaFormulario.forward(request, response);
			}
		case "borrar":
			dal.borrar(usuario);
			rutaListado.forward(request, response);
		}
	}

}