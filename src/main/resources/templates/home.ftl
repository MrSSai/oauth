<!DOCTYPE html>
<html lang="en">
<head>
    <title>首页</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <#-- 支持移动端设备自适应 -->
    <meta name="viewport" content="width=device-width, initial">
</head>

<body>
<#--
<div>登录成功</div>
<div>${userinfo.nickname}</div>
<div><img src="${userinfo.headimgurl}" width="100" height="100"></div>
-->

<div>授权成功</div>
<div>
    ${userEntity.nickname?if_exists}
</div>
<div>
    <img src="${userEntity.image?if_exists}" width="100" height="100">
</div>
<div>
    ${userEntity.email?if_exists}
</div>

</body>
</html>