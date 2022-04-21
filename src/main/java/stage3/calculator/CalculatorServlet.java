package stage3.calculator;

import org.mariuszgromada.math.mxparser.Expression;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CalculatorServlet extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String str = request.getParameter("str");
        if(str == null)
            str = "";

        String answer = "";
        if(str != null && str != ""){
            answer = Double.toString(calculate(str));
        }

        request.setAttribute("answer", answer);
        request.setAttribute("str", str);

        getServletContext().getRequestDispatcher("/calculator.jsp").forward(request,response);
    }
    protected double calculate(String str){
        Expression expression = new Expression(str);
        return expression.calculate();
    }
}
