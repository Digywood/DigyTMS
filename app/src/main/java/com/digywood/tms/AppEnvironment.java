package com.digywood.tms;

public enum AppEnvironment {

    DEBUG {
        @Override
        public boolean debug() {
            return true;
        }
    },
    PRODUCTION {
        @Override
        public boolean debug() {
            return false;
        }
    };


    public abstract boolean debug();


}
