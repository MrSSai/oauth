<html>
<title>注册</title>
<body>
<h1>第一次授权，请绑定帐号</h1>
<div>
    ${userEntity.nickname?if_exists}
</div>
<div>
    <img src="${userEntity.image?if_exists}">
</div>
<form action="/regsave" method="get">
    <input type="hidden" name="openid" value="${userEntity.openid?if_exists}" />
    <div>电话:<input type="text" name="phone" value="" /></div>
    <div>邮件:<input type="text" name="email" value="" /></div>
    <div><input type="submit" /></div>
</form>
</body>
</html>