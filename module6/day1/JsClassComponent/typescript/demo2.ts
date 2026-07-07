function sayhi(id:number,

               fname:string,

               lname?:string):void

{

    //?makes lname as optional

    console.log("ID:", id);

    console.log("Name",fname);

    if(lname!=undefined)

        console.log("lname",lname);

}

sayhi(123,"sachin");

sayhi(111,"fhsj","ganguly");
