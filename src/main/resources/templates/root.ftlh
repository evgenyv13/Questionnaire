<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Questionnaire</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-giJF6kkoqNQ00vy+HMDP7azOuL0xtbfIcaT9wjKHr8RbDVddVHyTfAAsrekwKmP1" crossorigin="anonymous">
    <link href="style.css" rel="stylesheet"/>
</head>
<body>
    <nav class="navbar sticky-top navbar-expand navbar-light bg-white">
        <div class="container">
            <a class="navbar-brand logo ms-5" href="">Questionnaire</a>
            <#if firstName?? && lastName??>
                <ul class="navbar-nav">
                    <li class="nav-item">
                        <a class="nav-link logo mx-3" href="/fields">Fields</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link logo mx-3" href="/responses">Responses</a>
                    </li>
                    <li class="nav-item dropdown ms-3">
                        <a class="nav-link dropdown-toggle logo text-primary" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                            ${firstName} ${lastName}
                        </a>
                        <ul class="dropdown-menu">
                            <li><a class="dropdown-item" href="/profile">Edit profile</a></li>
                            <li><a class="dropdown-item" href="/changepassword">Change password</a></li>
                            <li><a class="dropdown-item" href="/logout">Log out</a></li>
                        </ul>
                    </li>
                </ul>
            <#else>
                <a href="/login" class="nav-link logo">Log in</a>
            </#if>
        </div>
    </nav>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta1/dist/js/bootstrap.bundle.min.js" integrity="sha384-ygbV9kiqUc6oa4msXn9868pTtWMgiQaeYH7/t7LECLbyPA2x65Kgf80OJFdroafW" crossorigin="anonymous"></script>
</body>
</html>