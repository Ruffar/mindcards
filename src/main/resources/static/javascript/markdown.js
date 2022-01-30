
/*$(".description").load(function(event) {
    console.log("poopoo");
    var thisHtml = $(this).html();
    $(this).html(convertMarkdown(thisHtml));
    $(this).html("poop");
});*/

// Hyperlink Hover //
$(document).on("mouseover","a.cardHyperlink",function(event){

    var hyperlink = $(this);
    var cardDiv = $(this).closest(".card")
    var hoverCard = $(this).find(".hoverCard");
    if (hoverCard.length == 0) { //If there is no hoverCard inside of the hyperlink, create one

        var linkParam = hyperlink.attr("href").split("/");
        var cardType = linkParam[0];
        var cardId = linkParam[1];
        $.ajax({
            type: "GET",
            url: "/getCardElement",
            data: {cardType: cardType, cardId: cardId},

            success: function(response) {
               var newDiv = $("#cardTemplates").find(".hoverCard."+cardType).clone();
               newDiv.appendTo(hyperlink);
               updateCardElement(response,newDiv);
            }
        });
    }
});


// Markdown //

$(document).ready(function() {
    $(".card-view").map(function(){
        parseCardDescription($(this));
    });
});

function parseCardDescription(cardViewDiv) {
    var description = cardViewDiv.find(".description");
    var unescapedDesc = convertMarkdown(description.text());
    cardViewDiv.find(".unescapedDesc").html(unescapedDesc);
}

function convertMarkdown(text) {

    text = text.replace(/&/g, "&amp;").replace(/</g, "&lt;").replace(/>/g, "&gt;"); //Escape important HTML characters such as &, < and >

    text = parseBold(text);
    text = parseItalic(text);
    text = parseUnderline(text);
    text = parseStrikethrough(text);
    text = parseBlockquote(text);
    text = parseHorizontalRule(text);

    text = parseHyperlink(text);

    text = parseBulletList(text);
    text = parseNumberList(text);

    text = parseMathExpression(text);

    text = parseLineBreak(text);

    return text;

}

function parseBold(text) {
    return text.replace(/\*\*(.+?)\*\*/g, '<strong>$1</strong>');
}

function parseItalic(text) {
    return text.replace(/\*(.+?)\*/g, '<em>$1</em>');
}

function parseUnderline(text) {
    return text.replace(/__(.+?)__/g, '<u>$1</u>');
}

function parseStrikethrough(text) {
    return text.replace(/~~(.+?)~~/g, '<del>$1</del>');
}

function parseBlockquote(text) {
    return text.replace(/^&gt;(.+?)$/gm, '<blockquote>$1</blockquote>');
}

function parseHorizontalRule(text) {
    return text.replace(/^---$/gm, '<hr/>');
}

function parseHyperlink(text) {
    return text.replace(/\[(.+?)]\((.+?)\)/g, '<a class="cardHyperlink" href="$2">$1</a>');
}

function parseBulletList(text) {
    return text.replace(/^-(.+?)$/gm,'<ul><li>$1</li></ul>').replace(/<\/ul>(\s*?)<ul>/g,'');
}

function parseNumberList(text) {
    return text.replace(/^[0-9]+?\.(.+?)$/gm,'<ol><li>$1</li></ol>').replace(/<\/ol>(\s*?)<ol>/g,'');
}

function parseLineBreak(text) {
    return text.replace(/\>\n/g,'>').replace(/\n\</g,'<').replace(/\n/g,'<br/>');
}

//Math expressions
//Stack
class Stack {
    constructor() {
        this.items = [];
        this.top = -1;
    }

    isEmpty() {
        return this.top < 0;
    }

    push(item) {
        this.top += 1;
        this.items[this.top] = item;
    }

    peek() {
        if (!this.isEmpty()) {
            return this.items[this.top];
        }
        return null;
    }

    pop() {
        if (!this.isEmpty()) {
            this.top -= 1;
            return this.items[this.top+1];
        }
        return null;
    }
}

function parseMathExpression(text) {
    return text.replace(/\\\((.+?)\\\)/g,(match,$1)=> {
        var expression = match.slice(2,match.length-2);

        var symbolStack = new Stack();
            var isVariable = false;
            var isTerm = false;
            var output = "<span class='mathExp'>"
            for (let i = 0; i < expression.length; i++) {

                currentChar = expression[i];

                var isAlphanumeric = currentChar.match(/[a-z0-9]/i); //i means not case sensitive
                if (!isTerm && isAlphanumeric) {
                    output += "<span class='term'>";
                    isTerm = true;
                } else if (isTerm && !isAlphanumeric) {
                    output += "</span>";
                    isTerm = false;
                }

                var isAlphabet = currentChar.match(/[a-z]/i); //i means not case sensitive
                if (!isVariable && isAlphabet) {
                    output += "<span class='variables'>";
                    isVariable = true;
                } else if (isVariable && !isAlphabet) {
                    output += "</span>";
                    isVariable = false;
                }

                if (currentChar == '\\') {
                    if (expression.slice(i+1,i+5) == "frac") {
                        output += "<span class='frac'><span class='fracExp'>";
                        symbolStack.push("frac");
                        i += 4;
                    } else if (expression.slice(i+1,i+5) == "sqrt") {
                        output += "&#x221A;<span><hr class='sqrtLine'>";
                        symbolStack.push("sqrt");
                        i+=4;
                    } else if (expression.slice(i+1,i+4) == "int") {
                        output += "&#x222b;";
                        i += 3;
                    } else if (expression.slice(i+1,i+6) == "infty") {
                        output += "&#x221e;"
                        i += 5;
                    }
                } else if (currentChar == '^') {
                    output += "<sup>"
                    symbolStack.push("^");
                } else if (currentChar == '_') {
                    output += "<sub>"
                    symbolStack.push("_");
                } else if (currentChar == '{') {
                    symbolStack.push("{");
                } else {
                    if (currentChar == '}' && symbolStack.peek() == '{') {
                        symbolStack.pop();
                    } else {
                        output += currentChar;
                    }

                    if (!symbolStack.isEmpty()) {
                        var symbol = symbolStack.peek();
                        switch(symbol) {
                            case "frac":
                                output += "</span><hr class='fracLine'><span class='fracExp'>";
                                symbolStack.pop();
                                symbolStack.push("fracMid");
                                break;
                            case "fracMid":
                                output += "</span></span>";
                                symbolStack.pop();
                                break;
                            case "sqrt":
                                output += "</span>";
                                symbolStack.pop();
                                break;
                            case "^":
                                output += "</sup>";
                                symbolStack.pop();
                                break;
                            case "_":
                                output += "</sub>";
                                symbolStack.pop();
                                break;
                        }
                    }
                }

            }

            if (isVariable) {
                output += "</span>";
            }
            if (isTerm) {
                output += "</span>"
            }

            return output+"</span>";
    });
}