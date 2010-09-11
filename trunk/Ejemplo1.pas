PRogrAm Completo;
const c1=c2;
      c2='a';
      c3 = 23;
type t1=integer;
        t2=235..30;
	t3= array[3..as4] of char;
var v1:t1;
	 v2:integer;
	 :char;
	 v4:boolean;

(* PROCEDURE *)
{
procedure proce(var pv1,pv2:t1; pv3:integer);
const c1=c2;
      c2='a';
      c3 = 23;
type t1=integer;
	  t2=23..30;
	  t3=array[3..4]of char;
var v1:t1;
	 v2:integer;
	 v3:char;
	 v4:boolean;
	begin
                while (2=3) do
                    begin
                    dop:=2;
                    if(12<=30)
                            then if (2=2)
                                    then op:=2
                                    else op2:=3
                            else pv2:=3*3;
                    end;
		if (pv1=pv2)
			then pv3:=pv2+3;
		if (pv1>pv3)
			then pv1:=3-4;
	end;

function funct(pv1:integer; var p2:char):car;
const c1=c2;
      c2='a';
      c3 = 23;
type t1=integer;
	  t2=23..30;
	  t3=array[3..4]of char;
var v1:t1;
	 v2:integer;
	 v3:char;
	 v4:boolean;
	begin
		if not((fv1=2)or(fv2='a'))
			then funct:=2;
		if ((fv2='r')and(fv1<>0))
			then funct:=3;
	end;

Begin
write(funct(1,'a')); {Creo que eso es todo}
{END.}