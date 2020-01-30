FROM oracle/graalvm-ce:19.3.1
RUN gu install native-image
COPY build/libs/order-service-0.1-all.jar .
COPY reflection .
RUN native-image -cp ./order-service-0.1-all.jar

FROM frolvlad/alpine-glibc:glibc-2.30
HEALTHCHECK --start-period=30s --interval=5s CMD curl -f http://localhost:8080/health || exit 1
RUN apk add --no-cache curl libstdc++
CMD ./order-service-executable
COPY --from=0 /order-service-executable .
