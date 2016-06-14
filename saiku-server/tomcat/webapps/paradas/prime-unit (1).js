var _ = require('underscore');
var NumberTheory = require('number-theory');
var Lazy = require('lazy.js')

const P = [2,3,5,7,11,13,17,19,23,29];




var D = [];


Lazy.range(1000)
  .async()
  .each(addValue)
  .onComplete(function(){
  	var fs = require('fs');
	fs.writeFile("out3.json", JSON.stringify(D), function(err) {
	    if(err) {
	        return console.log(err);
	    }

	    console.log("The file was saved!");
	}); 
  });

  var PrimesUntil = 0;
  function addValue(k){
  		console.log(k)
  		var X = {};
  		X['$k'] = k;
  		PrimesUntil += (X['$p'] = NumberTheory.isPrime(k)|0);
  		X['$π(k)'] = PrimesUntil;
  		X['$k/π(k)'] = (k/X['$π(k)']) ||0;
  		_.each(NumberTheory.factor(k),function(factor){
  			X = _.clone(X);
  			X['power'] = factor.power;
  			X['prime'] = factor.prime;
  			console.log(X)
  			D.push(X);
  		})
  }