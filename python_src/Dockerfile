FROM python:3.6-slim-buster
COPY emoteDetection.py app/emoteDetection.py
COPY requirements.txt  app/requirements.txt
COPY models/*          app/models/

#RUN CPUCORES=$(getconf _NPROCESSORS_ONLN)

# Essential packages
RUN apt update && apt install -y libgtk-3-dev

# Compile Dlib using cmake
#RUN mkdir -p ~/dlib \
#    && cd ~/dlib \
#    && wget http://dlib.net/files/dlib-19.19.tar.bz2 \
#    && tar xf dlib-19.19.tar.bz2 \
#    && cd ~/dlib/dlib-19.19 \
#    && mkdir build \
#    && cd build \
#    && cmake .. -DUSE_SSE2_INSTRUCTIONS=ON \
#    && cmake --build . --config Release \
#    && make -j${CPUCORES} \
#    && cd ~/dlib/dlib-19.19/build \
#    && make install \
#    && ldconfig

# Install packages for Python
#RUN pkg-config --libs --cflags dlib-1 \
#    && cd ~/dlib/dlib-19.19 \
#    && python setup.py install --no DLIB_USE_CUDA --no USE_AVX_INSTRUCTIONS --no USE_SSE4_INSTRUCTIONS \
#    && rm -rf ~/dlib /var/cache/apk/* /usr/share/man /usr/local/share/man /tmp/*

# Run facial recog app
WORKDIR app/
RUN which python3
RUN python3 -m pip install --upgrade pip \
    && python3 -m pip install -r requirements.txt
ENTRYPOINT ["python3", "-u", "emoteDetection.py"]