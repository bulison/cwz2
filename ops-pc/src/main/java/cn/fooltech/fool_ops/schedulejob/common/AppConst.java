package cn.fooltech.fool_ops.schedulejob.common;

public class AppConst {


    public static final class Env {
        public static final String DEV = "dev";
        public static final String TEST = "test";
        public static final String PROD = "prod";
    }

    public static final class HttpMethod {
        public static final String GET = "get";
        public static final String POST = "post";
    }

    public static final class JobType {
        public static final String HTTP_JOB = "http_job";
        public static final String THRIFT_JOB = "thrift_job";
    }
}
