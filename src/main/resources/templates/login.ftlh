<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Log In</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-giJF6kkoqNQ00vy+HMDP7azOuL0xtbfIcaT9wjKHr8RbDVddVHyTfAAsrekwKmP1" crossorigin="anonymous">
    <link href="style.css" rel="stylesheet"/>
</head>
<body>
    <div class="container fullscreen">
        <div class="vc">
            <form class="fw form-control p-4 text-center" action="/login/process" method="post">
                <h2 class="mb-3 logo">Questionnaire</h2>
                <h2 class="mb-3">Log In</h2>
                <#if messageType?? && message??>
                    <div class="alert alert-${messageType}">${message}</div>
                </#if>
                <div class="mb-3">
                    <input type="email" name="email" value="${email!''}" placeholder="Email" class="form-control">
                </div>
                <div class="mb-3">
                    <input type="password" name="password" placeholder="Password" class="form-control"/>
                </div>
                <div class="row mb-2 p-0">
                    <div class="pe-0 col text-start">
                        <div class="form-check">
                            <input class="form-check-input" type="checkbox" name="remember-me" value="">
                            <label class="form-check-label">Remember me</label>
                        </div>
                    </div>
                    <div class="ps-0 col text-end">
                        <a href="/forgotpassword">Forgot your password?</a>
                    </div>
                </div>
                <div class="row fsb">
                    <button type="submit" class="btn btn-primary mb-2 ps-0 pe-0">LOG IN</button><br>
                </div>
                Don't have account?
                <a href="/signup">Sign Up</a>
            </form>
        </div>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta1/dist/js/bootstrap.bundle.min.js" integrity="sha384-ygbV9kiqUc6oa4msXn9868pTtWMgiQaeYH7/t7LECLbyPA2x65Kgf80OJFdroafW" crossorigin="anonymous"></script>
</body>
</html>