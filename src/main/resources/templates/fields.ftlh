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
        <a class="navbar-brand logo ms-5" href="/">Questionnaire</a>
        <ul class="navbar-nav">
            <li class="nav-item">
                <a class="nav-link logo mx-3 text-primary" href="/fields">Fields</a>
            </li>
            <li class="nav-item">
                <a class="nav-link logo mx-3" href="/responses">Responses</a>
            </li>
            <li class="nav-item dropdown ms-3">
                <a class="nav-link dropdown-toggle logo" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                    ${firstName} ${lastName}
                </a>
                <ul class="dropdown-menu">
                    <li><a class="dropdown-item" href="/profile">Edit profile</a></li>
                    <li><a class="dropdown-item" href="/changepassword">Change password</a></li>
                    <li><a class="dropdown-item" href="/logout">Log out</a></li>
                </ul>
            </li>
        </ul>
    </div>
</nav>
<div class="container">
    <div class="form-control mt-4 px-4 justify-content-center">
        <div class="row border-bottom">
            <div class="col">
                <h3 class="my-2">Fields</h3>
            </div>
            <button class="btn btn-primary col-1 m-2" data-bs-toggle="modal" data-bs-target="#addFieldModal">ADD FIELD</button>
            <div class="modal fade" id="addFieldModal" tabindex="-1" aria-hidden="true">
                <div class="modal-dialog modal-dialog-centered">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title" id="exampleModalLabel">Add field</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <form method="post" action="/fields/add">
                            <div class="modal-body">
                                <div class="container">
                                    <div class="row mb-3">
                                        <div class="col-3 text-secondary text-end">
                                            Label
                                            <span class="text-danger">*</span>
                                        </div>
                                        <div class="col-7">
                                            <input class="form-control" name="label" required>
                                        </div>
                                    </div>
                                    <div class="row mb-3">
                                        <div class="col-3 text-secondary text-end">
                                            Type
                                            <span class="text-danger">*</span>
                                        </div>
                                        <div class="col-7">
                                            <select class="form-select" name="type" required>
                                                <option value="sl">Single line text</option>
                                                <option value="ml">Multi line text</option>
                                                <option value="rb">Radio button</option>
                                                <option value="chb">Check box</option>
                                                <option value="comb">Combobox</option>
                                                <option value="dt">Date</option>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="row mb-3">
                                        <div class="col-3 text-secondary text-end">
                                            Options
                                            <span class="text-danger">*</span>
                                        </div>
                                        <div class="col-7">
                                            <textarea class="form-control" name="options" required></textarea>
                                        </div>
                                    </div>
                                    <div class="row mb-3">
                                        <div class="col-3"></div>
                                        <div class="col-7">
                                            <div class="row">
                                                <div class="col">
                                                    <div class="form-check">
                                                        <input class="form-check-input" type="checkbox" name="required">
                                                        <label class="form-check-label text-secondary">Required</label>
                                                    </div>
                                                </div>
                                                <div class="col">
                                                    <div class="form-check">
                                                        <input class="form-check-input" type="checkbox" name="isActive">
                                                        <label class="form-check-label text-secondary">Is Active</label>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">CANCEL</button>
                                <button type="submit" class="btn btn-primary">SAVE</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
        <table class="table table-striped">
            <thead>
                <tr>
                    <th scope="col">Label</th>
                    <th scope="col">Type</th>
                    <th scope="col">Required</th>
                    <th scope="col">Is Active</th>
                    <th scope="col"></th>
                </tr>
            </thead>
            <tbody>
                <#list fields as field>
                    <tr>
                        <td>${field.label}</td>
                        <td>${field.type}</td>
                        <td><#if field.required>True<#else>False</#if></td>
                        <td><#if field.active>True<#else>False</#if></td>
                        <td>
                            <button class="btn shadow-none" data-bs-toggle="modal" data-bs-target="#editField${field?index}">
                                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-pencil-square" viewBox="0 0 16 16">
                                    <path d="M15.502 1.94a.5.5 0 0 1 0 .706L14.459 3.69l-2-2L13.502.646a.5.5 0 0 1 .707 0l1.293 1.293zm-1.75 2.456l-2-2L4.939 9.21a.5.5 0 0 0-.121.196l-.805 2.414a.25.25 0 0 0 .316.316l2.414-.805a.5.5 0 0 0 .196-.12l6.813-6.814z"/>
                                    <path fill-rule="evenodd" d="M1 13.5A1.5 1.5 0 0 0 2.5 15h11a1.5 1.5 0 0 0 1.5-1.5v-6a.5.5 0 0 0-1 0v6a.5.5 0 0 1-.5.5h-11a.5.5 0 0 1-.5-.5v-11a.5.5 0 0 1 .5-.5H9a.5.5 0 0 0 0-1H2.5A1.5 1.5 0 0 0 1 2.5v11z"/>
                                </svg>
                            </button>
                            <button class="btn shadow-none">
                                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-trash" viewBox="0 0 16 16">
                                    <path d="M5.5 5.5A.5.5 0 0 1 6 6v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5zm2.5 0a.5.5 0 0 1 .5.5v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5zm3 .5a.5.5 0 0 0-1 0v6a.5.5 0 0 0 1 0V6z"/>
                                    <path fill-rule="evenodd" d="M14.5 3a1 1 0 0 1-1 1H13v9a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V4h-.5a1 1 0 0 1-1-1V2a1 1 0 0 1 1-1H6a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1h3.5a1 1 0 0 1 1 1v1zM4.118 4L4 4.059V13a1 1 0 0 0 1 1h6a1 1 0 0 0 1-1V4.059L11.882 4H4.118zM2.5 3V2h11v1h-11z"/>
                                </svg>
                            </button>
                            <div class="modal fade" id="editField${field?index}" tabindex="-1" aria-hidden="true">
                                <div class="modal-dialog modal-dialog-centered">
                                    <div class="modal-content">
                                        <div class="modal-header">
                                            <h5 class="modal-title" id="exampleModalLabel">Edit field</h5>
                                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                        </div>
                                        <form method="post" action="/fields/edit">
                                            <div class="modal-body">
                                                <div class="container">
                                                    <input type="hidden" name="fid" value="${field.id}">
                                                    <div class="row mb-3">
                                                        <div class="col-3 text-secondary text-end">
                                                            Label
                                                            <span class="text-danger">*</span>
                                                        </div>
                                                        <div class="col-7">
                                                            <input class="form-control" name="label" value="${field.label}" required>
                                                        </div>
                                                    </div>
                                                    <div class="row mb-3">
                                                        <div class="col-3 text-secondary text-end">
                                                            Type
                                                            <span class="text-danger">*</span>
                                                        </div>
                                                        <div class="col-7">
                                                            <select class="form-select" name="type" required>
                                                                <option value="sl" <#if field.type == "Single line text">selected</#if>>Single line text</option>
                                                                <option value="ml" <#if field.type == "Multi line text">selected</#if>>Multi line text</option>
                                                                <option value="rb" <#if field.type == "Radio button">selected</#if>>Radio button</option>
                                                                <option value="chb" <#if field.type == "Check box">selected</#if>>Check box</option>
                                                                <option value="comb" <#if field.type == "Combobox">selected</#if>>Combobox</option>
                                                                <option value="dt" <#if field.type == "Date">selected</#if>>Date</option>
                                                            </select>
                                                        </div>
                                                    </div>
                                                    <div class="row mb-3">
                                                        <div class="col-3 text-secondary text-end">
                                                            Options
                                                            <span class="text-danger">*</span>
                                                        </div>
                                                        <div class="col-7">
                                                            <textarea class="form-control" name="options" required></textarea>
                                                        </div>
                                                    </div>
                                                    <div class="row mb-3">
                                                        <div class="col-3"></div>
                                                        <div class="col-7">
                                                            <div class="row">
                                                                <div class="col">
                                                                    <div class="form-check">
                                                                        <input class="form-check-input" type="checkbox" name="required">
                                                                        <label class="form-check-label text-secondary">Required</label>
                                                                    </div>
                                                                </div>
                                                                <div class="col">
                                                                    <div class="form-check">
                                                                        <input class="form-check-input" type="checkbox" name="isActive">
                                                                        <label class="form-check-label text-secondary">Is Active</label>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="modal-footer">
                                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">CANCEL</button>
                                                <button type="submit" class="btn btn-primary">SAVE</button>
                                            </div>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </td>
                    </tr>
                </#list>
            </tbody>
        </table>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta1/dist/js/bootstrap.bundle.min.js" integrity="sha384-ygbV9kiqUc6oa4msXn9868pTtWMgiQaeYH7/t7LECLbyPA2x65Kgf80OJFdroafW" crossorigin="anonymous"></script>
</body>
</html>