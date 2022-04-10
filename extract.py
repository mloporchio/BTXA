"""
    File: extract.py
    Author: Matteo Loporchio

    Usage: extract <inputFileName> <outputFileName> <type> <minAmount> <maxAmount>

    Parses <inputFileName> to extract all inputs or outputs
    within the range [<minAmount>, <maxAmount>].

    Information about inputs or outputs is written to
    <outputFileName> in CSV format.

    <type> can be either 'in' or 'out', for parsing TX inputs or outputs,
    respectively.
"""

import os
import sys
import time

def extract(inputFileName, outputFileName, type, minAmount, maxAmount):
    start = time.time_ns()
    # Open input and output files.
    inputFile = open(inputFileName, 'r')
    outputFile = open(outputFileName, 'w')
    #
    inputSize = os.path.getsize(inputFileName)
    print('Input size: {} bytes'.format(inputSize))
    # Write the first line (CSV header).
    if type == 'in':
        outputFile.write(
        'timestamp,blockId,txId,address,amount,prevTxId,prevTxOffset\n')
    else:
        outputFile.write(
        'timestamp,blockId,txId,address,amount,scriptType,offset\n')
    #
    count = 0
    while True:
        # Read a line from input file.
        line = inputFile.readline()
        if not line:
            break
        # Split the line and obtain info, inputs, and outputs.
        parts = line.split(':')
        infos = parts[0].split(',')
        inputs = parts[1].split(';')
        outputs = parts[2].split(';')
        # Parse the associated information.
        timestamp = int(infos[0])
        blockId = int(infos[1])
        txId = int(infos[2])
        # Extract inputs or outputs from TX data.
        if type == 'in':
            if parts[1] != '':
                for i in range(0, len(inputs)):
                    inputParts = inputs[i].split(',')
                    address = int(inputParts[0])
                    amount = int(inputParts[1])
                    prevTxId = int(inputParts[2])
                    prevTxOffset = int(inputParts[3])
                    # Print the TX input to file only if its amount matches
                    # the desired condition.
                    if (minAmount <= amount and amount <= maxAmount):
                        outputFile.write('{},{},{},{},{},{},{}\n'.format(
                        timestamp, blockId, txId, address, amount, prevTxId,
                        prevTxOffset))
        # Extract outputs from TX data.
        else:
            if parts[2] != '':
                for offset in range(0, len(outputs)):
                    outputParts = outputs[offset].split(',')
                    address = int(outputParts[0])
                    amount = int(outputParts[1])
                    scriptType = int(outputParts[2])
                    # Write the TX output to file only if the corresponding
                    # amount is in the desired range.
                    if (minAmount <= amount and amount <= maxAmount):
                        outputFile.write("{},{},{},{},{},{},{}\n".format(
                        timestamp, blockId, txId, address, amount, scriptType,
                        offset))
        count += 1
        print('Progress: {}%'.format(int(100*inputFile.tell()/inputSize)),
        end='\r')
    # Close the files.
    inputFile.close()
    outputFile.close()
    end = time.time_ns()
    print('\nProcessed lines: {}\nElapsed time: {} ms'.format(
    count, int((end - start) / 1e06)))

def main(argv):
    if len(argv) < 6:
        print("Error: too few arguments!")
        return
    inputFile = argv[1]
    outputFile = argv[2]
    type = argv[3]
    if (type != 'in' and type != 'out'):
        raise ValueError('Error: type must be either \'in\' or \'out\'!')
    minAmount = int(argv[4])
    maxAmount = int(argv[5])
    extract(inputFile, outputFile, type, minAmount, maxAmount)

if __name__ == '__main__':
    main(sys.argv)
