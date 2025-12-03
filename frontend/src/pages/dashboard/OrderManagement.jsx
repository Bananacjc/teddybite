import React from 'react';
import { Box, Heading, Table, Thead, Tbody, Tr, Th, Td, Badge, Button } from '@chakra-ui/react';

const OrderManagement = () => {
  return (
    <Box>
      <Heading size="lg" color="brown.900" mb={6}>Order Management</Heading>

      <Box bg="white" borderRadius="xl" boxShadow="sm" overflowX="auto">
        <Table variant="simple">
          <Thead>
            <Tr>
              <Th>Order ID</Th>
              <Th>Date & Time</Th>
              <Th>Customer</Th>
              <Th>Items</Th>
              <Th isNumeric>Total (RM)</Th>
              <Th>Status</Th>
              <Th>Action</Th>
            </Tr>
          </Thead>
          <Tbody>
            <Tr>
              <Td fontWeight="bold">#ORD-001</Td>
              <Td>Oct 24, 2024 12:30 PM</Td>
              <Td>Walk-in</Td>
              <Td>2x Burger, 1x Coke</Td>
              <Td isNumeric>45.50</Td>
              <Td><Badge colorScheme="green">Completed</Badge></Td>
              <Td><Button size="sm">View</Button></Td>
            </Tr>
            <Tr>
              <Td fontWeight="bold">#ORD-002</Td>
              <Td>Oct 24, 2024 12:45 PM</Td>
              <Td>Walk-in</Td>
              <Td>1x Pizza</Td>
              <Td isNumeric>22.00</Td>
              <Td><Badge colorScheme="orange">Preparing</Badge></Td>
              <Td><Button size="sm">View</Button></Td>
            </Tr>
          </Tbody>
        </Table>
      </Box>
    </Box>
  );
};

export default OrderManagement;
