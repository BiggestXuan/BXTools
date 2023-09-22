$(function(){
    $("#mj_bt").click(function(){
        var v1 = parseInt($("#mj1").val())
        var v2 = parseInt($("#mj2").val())
        var v3 = parseInt($("#mj3").val())
        var v4 = parseInt($("#mj4").val())
        var base = parseInt($("#mj_base").val())
        var NameArray = ["对家","上家","下家","本家"]
        var PlayerArray = [v1,v2,v3,v4]
        var WinnerArray = []
        var LoserArray = []
        let total = 0
        var index = 0
        PlayerArray.forEach(e => {
            var playerClass = {
                "score":e - base,
                "name":NameArray[index]
            }
            index++
            e >= base ? WinnerArray.push(playerClass) : LoserArray.push(playerClass)
            total += e
        });
        if(total != base*4){
            alert("四名玩家的积分相加不为初始配点，请检查！")
            return
        }
        //setGamePlayer(WinnerArray,"#mj_winner")
        //setGamePlayer(LoserArray,"#mj_loser")
        setResult(calc(WinnerArray,LoserArray))
    })
})

function setResult(res){
    var t =[]
    t.push("<br>")
    res.forEach(player => {
        var y = player.name+" -> "+player.to+" Money: "+player.reward * Number($("#mj_rate").val())+"<br/>"
        console.log(y)
        t.push(y)
    })
    $("#result").html(t)
}

function calc(WinnerArray,LoserArray){
    var out = []
    LoserArray.forEach(loser => {
        WinnerArray.forEach(winner => {
            if(winner.score != 0 && loser.score != 0){
                out.push({
                    "name":loser.name,
                    "to":winner.name,
                    "reward":min(-loser.score,winner.score)
                })
                if(-loser.score == winner.score){
                    
                }else if(-loser.score < winner.score){
                    winner.score += loser.score
                    loser.score = 0
                }else{
                    loser.score += winner.score
                    winner.score = 0
                }
            }
        })
    });
    return out
}

function min(a,b){
    return a <= b ? a : b
}

function setGamePlayer(array,id){
    let arr = []
    array.forEach(e => {
        arr.push(e.name+" ")
    });
    $(id).html(arr)
}