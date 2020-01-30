FROM oracle/graalvm-ce:19.3.1
COPY build/libs/order-service-0.1-all.jar .
COPY reflection .
RUN gu install native-image
RUN native-image -cp ./order-service-0.1-all.jar

FROM frolvlad/alpine-glibc:glibc-2.30
RUN apk add --no-cache gcc
RUN apk add curl
COPY --from=0 /order-service-executable .
CMD ./order-service-executable
HEALTHCHECK --start-period=30s --interval=5s CMD curl -f http://localhost:8080/health || exit 1