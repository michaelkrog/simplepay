<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt"  prefix="fmt" %>
<!DOCTYPE html>
<html lang="da">
  <head>
    <meta charset="utf-8">
    <title></title>
    <jsp:include page="inc/head.jsp" />
  </head>  
    <div class="container">
            
            <jsp:include page="inc/top.jsp" />
            
            <div id="contentwrapper" class="content">
                <div class="row">
                    <div class="span12">
                        <h1 style="padding:20px;background: #FAFAFA;border-bottom: 1px solid #DFDFDF;">Transaktioner</h1>
                        <table class="table">
                            <thead>
                            <tr>
                                <th>Ordernummer</th>
                                <th>Beløb</th>
                                <th>Status</th>
                            </tr>
                            <tbody>
                                <c:forEach var="transaction" items="${transactions}">
                                    <tr>
                                      <td>${transaction.orderNumber}</td>
                                      <td><fmt:formatNumber value='${1.0 * transaction.authorizedAmount}' currencyCode="${transaction.currency}" type='currency'/></td>
                                      <td>${transaction.status}</td>
                                    </tr>
                                </c:forEach>
                          </thead>
                        </table>
                        
                    </div>
                </div> 
                
                
                <jsp:include page="inc/footer.jsp" />
            </div>
	</div>
        <jsp:include page="inc/scripts.jsp" />
        
</body>
</html>