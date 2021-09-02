<html>
<head>
  <title>VPlanBot</title>
</head>

<style>
.styled-table {
    border-collapse: collapse;
    margin: 25px 0;
    font-size: 0.9em;
    font-family: sans-serif;
    min-width: 400px;
    /*box-shadow: 0 0 20px rgba(0, 0, 0, 0.15);*/
    margin-top: 0px;
    margin-bottom: 0px;
}
.styled-table thead tr {
    background-color: #009879;
    color: #ffffff;
    text-align: left;
}
.styled-table th,
.styled-table td {
    padding: 12px 15px;
}
.styled-table tbody tr {
    border-bottom: 1px solid #dddddd;
}

.styled-table tbody tr:nth-of-type(even) {
    background-color: #f3f3f3;
}

.styled-table tbody tr:last-of-type {
    border-bottom: 2px solid #009879;
}

.styled-table tbody tr.failure td:not(:last-child){
    font-weight: bold;
    color: #b4332a;
    text-decoration: line-through;
}
.styled-table tbody tr.failure td:last-child{
    font-weight: bold;
    color: #b4332a;
}
.styled-table tbody tr.change {
    color: orange;
}
p {
    font-size: 0.9em;
    font-family: sans-serif;
}
body{
    background: #FFFFFF;
    margin: 0px;

}
</style>

<body>
    <table class="styled-table">
        <thead>
            <tr>
                <th>${title_time}</th>
                <th>${title_subject}</th>
                <th>${title_teacher}</th>
                <th>${title_room}</th>
                <th>${title_info}</th>
            </tr>
        </thead>
        <tbody>
          <#list subjects as subject>
            <tr class="${subject.status}">
                <td>${subject.time}</td>
                <td>${subject.name}</td>
                <td>${subject.teacher}</td>
                <td>${subject.room}</td>
                <td>${subject.info}</td>
            </tr>
          </#list>
        </tbody>
    </table>
    <p style="font-weight: bold; color: #009879; font-size: 1.2em; margin-bottom: 0;">${info_title}</p>
    <p style="margin-top:  0; width: 45ch;">${info_text}</p>

</body>
</html>