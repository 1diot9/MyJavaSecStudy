with open("hex.txt", "r") as f:
    data = bytes.fromhex(f.read())
    open("hex.pcap", "wb").write(data)