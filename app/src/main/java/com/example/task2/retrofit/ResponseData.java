package com.example.task2.retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseData {

    @SerializedName("Response")
    @Expose
    private List<Response> response = null;

    /**
     * No args constructor for use in serialization
     *
     */
    public ResponseData() {
    }

    /**
     *
     * @param response
     */
    public ResponseData(List<Response> response) {
        super();
        this.response = response;
    }

    public List<Response> getResponse() {
        return response;
    }

    public void setResponse(List<Response> response) {
        this.response = response;
    }

    public class Response {

        @SerializedName("fname")
        @Expose
        private String fname;
        @SerializedName("lname")
        @Expose
        private String lname;
        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("number")
        @Expose
        private String number;
        @SerializedName("address")
        @Expose
        private String address;

        /**
         * No args constructor for use in serialization
         *
         */
        public Response() {
        }

        /**
         *
         * @param fname
         * @param number
         * @param lname
         * @param address
         * @param type
         */
        public Response(String fname, String lname, String type, String number, String address) {
            super();
            this.fname = fname;
            this.lname = lname;
            this.type = type;
            this.number = number;
            this.address = address;
        }

        public String getFname() {
            return fname;
        }

        public void setFname(String fname) {
            this.fname = fname;
        }

        public String getLname() {
            return lname;
        }

        public void setLname(String lname) {
            this.lname = lname;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

    }


}