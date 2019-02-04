package com.digywood.tms;

public enum UserMode {

    NON_PRIME {
        @Override
        public boolean mode() {
            return true;
        }
    },

    PRIME {
        @Override
        public boolean mode() {
            return false;
        }
    };

    public abstract boolean mode();


}
