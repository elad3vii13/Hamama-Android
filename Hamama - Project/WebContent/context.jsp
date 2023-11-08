<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ page import="logic.Context"%> <% Context ctx = null;
try{ ctx = new Context(pageContext); } catch(Exception e){
System.out.println("Fatal error to get 'Context' instance"); } %>
