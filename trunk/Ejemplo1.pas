program Completo;
const c1=20;
		c2='a';
type t1=integer;
	  t2=23..30;
	  t3=array[3..4]of char;
var v1:t1;
	 v2:integer;
	 v3:char;
	 v4:boolean;

procedure proce(var pv1,pv2:t1; pv3:integer);
	begin
		if(12<=30)
			then pv1:=1 div 2;
			else pv2:=3*3;
		if (pv1=pv2)
			then pv3:=pv2+3;
		if (pv1>pv3)
			then pv1:=3-4;
	end;
	
write  ('SOLO MINUSCULA.....S======>>');
	

function funct(var fv1:integer; fv2:char):integer;
	begin
		if not((fv1=2)or(fv2='a'))
			then funct:=2;
		if ((fv2='r')and(fv1<>0))
			then funct:=3;
	end;

begin
write(funct(1,'a')); {Creo que eso es todo}
END.
