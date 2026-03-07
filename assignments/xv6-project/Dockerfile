FROM ubuntu:22.04

RUN apt-get update && apt-get install -y \
gcc \
make \
git \
qemu-system-x86 \
build-essential

WORKDIR /xv6

RUN git clone https://github.com/mit-pdos/xv6-public.git .

CMD [ "/bin/bash" ]