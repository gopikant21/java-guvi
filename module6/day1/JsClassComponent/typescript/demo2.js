"use strict";
function sayhi(id, fname, lname) {
    //?makes lname as optional
    console.log("ID:", id);
    console.log("Name", fname);
    if (lname != undefined)
        console.log("lname", lname);
}
sayhi(123, "sachin");
sayhi(111, 357, "ganguly");
