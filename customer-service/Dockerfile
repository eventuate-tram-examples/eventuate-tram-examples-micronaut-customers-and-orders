FROM oracle/graalvm-ce
COPY build/libs/customer-service-0.1-all.jar .
COPY reflection .
RUN gu install native-image
RUN native-image -cp ./customer-service-0.1-all.jar
HEALTHCHECK --start-period=30s --interval=5s CMD curl -f http://localhost:8080/health || exit 1
CMD ./customer-service-executable