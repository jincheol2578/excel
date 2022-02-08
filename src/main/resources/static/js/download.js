$('#excel-down').on('click', ()=>{
    location.href='/excel/download';
})

function regBoard() {
    const param = {
        title: $('#title').val(),
        ctnt: $('#content').val(),
        writer: $('#writer').val()
    }

    const init = {
        method: 'POST',
        body: JSON.stringify(param),
        headers: {
            'accept': 'application/json',
            'content-type': 'application/json;charset=UTF-8'
        }
    }
    fetch("/excel", init)
        .then((res)=>{
            return res.json();
        })
        .then((data)=>{
            console.log(data);
            getList();
        })
}
function getList() {
    fetch('/excel').then((res)=>{
        return res.json();
    }).then((data)=>{
        makeList(data.data);
    });
}

getList();

function makeList(data){
    $('#data-list').text('');
    data.forEach((item)=>{
        const tr = $('<tr>')
        tr.append($('<td>').text(item.id));
        tr.append($('<td>').text(item.writer));
        tr.append($('<td>').text(item.title));
        tr.append($('<td>').text(item.ctnt));
        tr.append($('<td>').text(item.regdt));
        $('#data-list').append(tr);
    })
}