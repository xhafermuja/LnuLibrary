package com.example.lnulibrary;

    public class Member {
        private int id;
        private String personalNumber;
        private String name;
        private String password;
        private String salt;
        private String role;
        private boolean isActive;

        public Member() {
            this(-1, "","",  "", "", "", true);
        }
        public Member(int id, String personalNumber, String name, String password, String salt, String role, boolean isActive) {
            this.id = id;
            this.personalNumber = personalNumber;
            this.name = name;
            this.password = password;
            this.salt = salt;
            this.role = role;
            this.isActive = isActive;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getPersonalNumber(){
            return personalNumber;
        }

        public void setPersonalNumber(String personalNumber) {
            this.personalNumber = personalNumber;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getSalt() {
            return salt;
        }

        public void setSalt(String salt) {
            this.salt = salt;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public boolean getIsActive() {
            return isActive;
        }

        public void setIsActive(boolean active) {
            isActive = active;
        }
    }