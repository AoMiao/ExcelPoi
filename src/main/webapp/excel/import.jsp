<%@page contentType="text/html;charset=UTF-8" %>
<%-- <%@ taglib uri="/jstl1.1/fmt.tld" prefix="fmt"%>
<%@ taglib uri="/jstl1.1/fn.tld" prefix="fn"%>
<%@ taglib uri="/jstl1.1/core.tld" prefix="c"%> --%>

<%
  response.setHeader("progma", "no-cache");
  response.setHeader("Cache-Control", "no-cache");
  response.setDateHeader("Expires", 0);
%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
  <%-- <jsp:include page="/admin/include/page_edit_head.jsp">
    <jsp:param name="title" value="导入" />
  </jsp:include> --%>
</head>
<body style="width:750px; overflow-x:hidden;" path="${path}">

<div class="main-container" id="mainContainer">

  <div id="body">
    <form  class="form-horizontal" action="doImport" data-validate="true"  id="editForm" method="post"  enctype="multipart/form-data">
      <input type="hidden" name="id" value="${data.id}">
      <div class="main-contentWrap cm-f14">
        <table class="table">
          <tbody>
          <tr>
            <td>导入活动信息数据</td>
            <td class="col-xs-8">
               <input class="fileupload" id="fileupload2" type="file" name="files[]" role="_upload" data-show="false" data-target="picur2" accept="file/xlsx">
            </td>
          </tr>
          <tr>
            <td class="cm-tac" colspan="2" >
              <input id="sub" type="button" class="cm-btn-confirm-bg cm-btn-default cm-border0" onclick = "checkfile();" value="确定"/>
              &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
              &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
              <a href="downloadTemplate"class="cm-btn-confirm-bg cm-btn-default" >下载excel模板</a>
            </td>
          </tr>
          <tr>
            <td class="cm-tac" colspan="2" >
              <span style="color: red;">先点击上传，再点击确认</span>
            </td>
          </tr>
          </tbody>
        </table>
      </div>
    </form>
  </div>

  <p class="footer"></p>
</div>
<script src="/excelpoi/jquery.min.js"></script>
</body>
<script>
function checkfile(){
    var val = $("#picur2").val();
      $("#editForm").submit();
    /* if(""==val){
      alert("请选择导入文件！");
      return false;
    }else{
    } */
  };
	$(function() {
		/* if ("${result}") {
			alert("${result}");
		} */
	});
</script>
</html>